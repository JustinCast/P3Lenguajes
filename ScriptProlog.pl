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


cleanBD(X, Y, W) :-
  retractall(edge(X, Y, W)).






oh(0, 1, 1).
oh(1, 2, 3).
oh(1, 3, 3).
oh(2, 3, 3).
oh(4, 5, 3).


path([B | Rest], B, [B | Rest], Length, Length).
path([A | Rest], B, Path, CurrentLength, Length) :-
    oh(A, C, X),
    \+member(C, [A | Rest]),
    NewLength is CurrentLength + X,
    path([C, A | Rest], B, Path, NewLength, Length).

find_paths(A, B) :-
    path([A], B, Path, 0, _),
    reverse(Path, DirectPath),
    printPath(DirectPath),
    writef('\n'),
    !.

printPath([]).
printPath([X]) :-
    !, write(X).
printPath([X|T]) :-
    write(X),
    write(','),
    printPath(T).

add(X, Y, P) :-
  asserta(oh(X, Y, P)), !.
/* try goals like

  ?- shortest(1,5,Path,Length).

*/