create table respostas(
    id bigint not null auto_increment,
    mensagem text not null,
    topico_id bigint not null,
    data_criacao datetime not null,
    autor_id bigint not null,
    solucao boolean not null,

    primary key(id),
    constraint fk_respostas_topico_id foreign key(topico_id) references topicos(id),
    constraint fk_respostas_autor_id foreign key(autor_id) references usuarios(id)
);