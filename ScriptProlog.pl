/* prolog tutorial 2.15 Graph structures and paths */

edge(1,2,1).
edge(1,4,3.5).
edge(1,3,2.5).
edge(2,3,1).
edge(2,5,2.5).
edge(3,4,1).
edge(3,5,2.2).
edge(4,5,1).


load(File) :- 
  exists_file(File),
  consult(File).
  
connected(X,Y,L) :- edge(X,Y,L) ; edge(Y,X,L).

path(A,B,Path,Len) :-
       travel(A,B,[A],Q,Len), 
       reverse(Q,Path).

travel(A,B,P,[B|P],L) :- 
       connected(A,B,L).
travel(A,B,Visited,Path,L) :-
       connected(A,C,D),           
       C \== B,
       \+member(C,Visited),
       travel(C,B,[C|Visited],Path,L1),
       L is D+L1.  

shortest(A,B,Path,Length) :-
   setof([P,L],path(A,B,P,L),Set),
   Set = [_|_], % fail if empty
   minimal(Set,[Path,Length]).

minimal([F|R],M) :- min(R,F,M).

% minimal path
min([],M,M).
min([[P,L]|R],[_,M],Min) :- L < M, !, min(R,[P,L],Min). 
min([_|R],M,Min) :- min(R,M,Min).

:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/http_json)).
:- use_module(library(http/json_convert)).
:- use_module(library(http/json)).
:- use_module(library(http/http_cors)).

% :- http_handler(root(hello_world), say_hi, []).		% (1)
:- http_handler(root(resolve),handle,[]).
:- http_handler(root(add),add,[]).
:- http_handler(root(clean),cleanBD,[]).
:- use_module(library(http/http_cors)).
:- setting(http:cors, list(atom), [], ['http://localhost:4200']).
server(Port) :-
   http_server(http_dispatch,[port(Port)]).

add(Request) :-
  %http_read_json(Request, DictIn,[json_object(term)]),
  http_read_json(Request, DictIn, [json_object(dict)]),
  format(user_output,"DictIn is: ~p~n",[DictIn]),
  DictOut = DictIn,
  reply_json(DictOut),
  json_to_prolog(DictIn, PrologIn),
  asserta(edge(PrologIn.from, PrologIn.to, PrologIn.weight)).

handle(Request) :-
  cors_enable,
  http_read_json(Request, DictIn, [json_object(dict)]),
  json_to_prolog(DictIn, PrologIn),
  shortest(PrologIn.from, PrologIn.to, Path, Length),
  DictOut= json([path = Path, len = Length]),
  reply_json(DictOut).

cleanBD(Request) :-
  http_read_json(Request, DictIn, [json_object(dict)]),
  json_to_prolog(DictIn, PrologIn),
  retractall(edge(PrologIn.from, PrologIn.to, PrologIn.weight)).
    
/* try goals like

  ?- shortest(1,5,Path,Length).

*/