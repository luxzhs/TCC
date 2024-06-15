#language: pt
Funcionalidade: Buscar por um produto na Amazon

Cenário: Usuário realiza diversas ações com um produto
  Dado que o usuário está na página inicial da Amazon
  Quando o usuário digita "notebook" na barra de busca
  E o usuário clica no botão de busca
  Então os resultados da busca por "notebook" são exibidos
  Quando o usuário aplica o filtro "Novo"
  Então os resultados da busca são filtrados para mostrar apenas produtos elegíveis para Novo
  Quando o usuário seleciona um produto dos resultados da busca
  Então a página de detalhes do produto é exibida
  E o título, preço e descrição do produto são visíveis
  Quando o usuário clica no botão "Adicionar ao Carrinho"
  Quando o usuário navega para a página do carrinho
  E o usuário clica no botão "Excluir" para o produto
  E o carrinho é atualizado de acordo


Cenário: Usuário visualiza os detalhes de um produto
Dado que o usuário está na página inicial 
Quando o usuário busca por "Iphone"
E o usuário aplica o filtro do celular "Mais caros primeiro"
E o usuário seleciona o filtro "Exibir itens em estoque"
Então o produto é adicionado  
E é possível visualizar o produto no carrinho



