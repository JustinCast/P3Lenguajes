import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { RouteInterface } from './models/Route.interface';

@Injectable({
  providedIn: 'root'
})
export class PrologCommunicationService {
  route: RouteInterface;
  constructor(private _http: HttpClient) { }

  getRoute(from: number, to: number) {
    let headers = new HttpHeaders().set('Content-Type', 'application/json; charset=UTF-8')
    .set("Access-Control-Allow-Credentials", "true")
    .set("Access-Control-Allow-Origin", "http://localhost:4200");
    // headers.set("Access-Control-Allow-Origin", "http://localhost:4200");
    // headers.set(
    //   "Access-Control-Allow-Methods",
    //   "GET, POST, PUT, DELETE, OPTIONS"
    // );
    // headers.set(
    //   "Access-Control-Allow-Headers",
    //   "Origin, X-Requested-With, Content-Type, Accept, Authorization, Access-Control-Allow-Credentials"
    // );
    // headers.set("Access-Control-Allow-Credentials", "true");
    this._http.post<RouteInterface>('http://localhost:3000/resolve', {"from": 1, "to": 5})
    .subscribe(
      route => {
        this.route = route;
        console.log(this.route);
      },
      (err: HttpErrorResponse) => this.handleError(err)
    ) 
  }

  handleError(err: HttpErrorResponse) {
    if (err.error instanceof Error) {
      // Error del lado del cliente
      console.log("An error occurred:", err.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // Error del lado del backend
      console.log(
        `Backend returned code ${err.status}, body was: ${JSON.stringify(
          err.error
        )}`
      );
    }
  }
}
