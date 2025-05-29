package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {

    private final Scanner sc = new Scanner(System.in);

    public void execute() throws SQLException{
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;

        while(true) {
            System.out.println(("1 - Criar um novo board"));
            System.out.println(("2 - Selecionar um board existente"));
            System.out.println(("3 - Excluir um board"));
            System.out.println(("4 - Sair"));
            option = sc.nextInt();

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void deleteBoard() throws SQLException{
        System.out.println("Informe o id do board que será excluído");
        var id = sc.nextLong();

        try(var connection = getConnection()) {
            var service = new BoardService(connection);
            if(service.delete(id)) {
                System.out.printf("O board %s foi excluído\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %s \n", id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectBoard() throws SQLException{
        System.out.print("Informe o id do board que deseja selecionar: ");
        var id = sc.nextLong();

        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrado um board com id %s\n", id)
            );

        }
    }

    private void createBoard() throws SQLException{
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu board:");
        var name = sc.nextLine();
        entity.setName(name);

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim, informar quantas, se não digite 0");
        var colunas = sc.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();
        System.out.print("Informe o nome da  coluna inicial do board: ");
        var initialComunName = sc.next();
        var inicialColumn = createColumn(initialComunName, BoardColumnKindEnum.INITIAL, 0);
        columns.add(inicialColumn);

        for(int i = 0; i < colunas; i++) {
            System.out.print("Informe o nome da coluna de tarefa pendente do board: ");
            var pendingColumnName = sc.next();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnKindEnum.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.print("Informe o nome da coluna final: ");
        var finalColumnName = sc.next();
        var finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, colunas + 1);
        columns.add(finalColumn);

        System.out.print("Informe o nome da coluna de cancelamento do baord: ");
        var cancelColumnName = sc.next();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, colunas + 1);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);

        try(var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);

        return boardColumn;
    }
}
