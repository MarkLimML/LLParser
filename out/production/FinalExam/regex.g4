//grammar representation

grammar regex;

expr
    : id union expr
    | LPAREN expr RPAREN symbol union expr
    | /*E*/
    ;

symbol
    : '*'
    | '+'
    | '?'
    | /*E*/
    ;

union
    : 'U'
    | /*E*/
    ;

id
    : TERMINAL symbol
    | 'E'
    ;

TERMINAL
    : [a-z]
    | [0-9]
    ;


LPAREN: '(';
RPAREN: ')';

WS  :  [ \t\r\n\u000C]+ -> skip
    ;