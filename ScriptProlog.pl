/* prolog tutorial 2.15 Graph structures and paths */
edge(b,a,0).

cleanBD(X, Y, W) :-
  retractall(edge(X, Y, W)).

add(X, Y, P) :-
  asserta(edge(X, Y, P)), !.
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