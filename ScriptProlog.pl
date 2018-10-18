/* prolog tutorial 2.15 Graph structures and paths */

edge(0,0,0).


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






oh(0, 0, 0).

path([B | Rest], B, [B | Rest], Length, Length).
path([A | Rest], B, Path, CurrentLength, Length) :-
    oh(A, C, X),
    \+member(C, [A | Rest]),
    NewLength is CurrentLength + X,
    path([C, A | Rest], B, Path, NewLength, Length).

find_paths(A, B, X) :-
    path([A], B, Path, 0, _),
    reverse(Path, DirectPath),
    %printPath(DirectPath),
    X = DirectPath.

printPath([]).
printPath([X]) :-
    !, write(X).
printPath([X|T]) :-
    write(X),
    write(','),
    printPath(T).

add(X, Y, P) :-
  asserta(edge(X, Y, P)), !.
/* try goals like

  ?- shortest(1,5,Path,Length).

*/

:-dynamic
 rpath/2.      % A reversed path


path(From,To,Dist) :- edge(To,From,Dist).
path(From,To,Dist) :- edge(From,To,Dist).

shorterPath([H|Path], Dist) :-         % path < stored path? replace it
 rpath([H|_], D), !, Dist < D,          % match target node [H|_]
 retract(rpath([H|_],_)),
 assert(rpath([H|Path], Dist)).
shorterPath(Path, Dist) :-         % Otherwise store a new path
 assert(rpath(Path,Dist)).

traverse(From, Path, Dist) :-      % traverse all reachable nodes
 path(From, T, D),      % For each neighbor
 not(memberchk(T, Path)),     % which is unvisited
 shorterPath([T,From|Path], Dist+D), % Update shortest path and distance
 traverse(T,[From|Path],Dist+D).     % Then traverse the neighbor

traverse(From) :-
 retractall(rpath(_,_)),           % Remove solutions
 traverse(From,[],0).              % Traverse from origin
traverse(_).

go(From, To,Path) :-
 traverse(From),                   % Find all distances
 rpath([To|RPath],_)->         % If the target was reached
   reverse([To|RPath], Path).