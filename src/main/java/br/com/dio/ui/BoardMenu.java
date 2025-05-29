package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardMenu {
   private final BoardEntity entity;

    public void execute() {
        System.out.printf("Bem vindo ao board %s, selecione a opção desejada:", entity.getId());
    }
}
