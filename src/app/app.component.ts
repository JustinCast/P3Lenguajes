import { Component, OnInit } from "@angular/core";
import { PrologCommunicationService } from "./prolog-communication.service";
@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  animations: []
})
export class AppComponent implements OnInit {
  state: Array<string> = [];
  values: Array<number> = [];
  title = "P3Lenguajes";
  interval;
  disabled: boolean = false;
  begin: number;
  end: number;

  constructor(private _prolog: PrologCommunicationService)
  {}

  ngOnInit() {
    for (let i = 1; i <= 36; i++) {
      this.values.push(i);
    }
  }

  selectParams(index: number) {
    if (!this.begin) this.begin = index - 1;
    else {
      this.end = index;
      this.disabled = true;
      this.setBee(this.end);
      this.start();
    }
  }

  start() {
    this.interval = setInterval(() => {
      let style = document.createElement("style");
      style.type = "text/css";
      style.innerHTML =
        ".background { background-image: url('../assets/spider.png'); background-repeat: no-repeat}";
      document.getElementsByTagName("head")[0].appendChild(style);

      let prev = document.getElementById(`${this.begin - 1}`);
      prev.classList.remove("background");

      let next = document.getElementById(`${this.begin}`);
      //console.log(next)
      next.classList.add("background");
      next.classList.add("animated");
      next.classList.add("shake");
      this.begin++;
      if (this.begin === this.end) {
        clearInterval(this.interval);
        this.setFinal();
        return;
      }
    }, 1000);
  }

  consultProlog() {
    this._prolog.getRoute(this.begin, this.end);
  }

  setBee(index: number) {
    let style = document.createElement("style");
    style.type = "text/css";
    style.innerHTML =
      ".bee { background-image: url('../assets/bee.png'); background-repeat: no-repeat; background-position: center}";
    document.getElementsByTagName("head")[0].appendChild(style);
    let next = document.getElementById(`${index}`);
    next.classList.add("bee");
    next.classList.add("animated");
    next.classList.add("tada");
    next.classList.add("infinite");
  }

  setFinal() {
    let style = document.createElement("style");
    style.type = "text/css";
    style.innerHTML =
      ".background { background-image: url('../assets/end.png'); background-repeat: no-repeat; background-position: center}";
    document.getElementsByTagName("head")[0].appendChild(style);
    let next = document.getElementById(`${this.end}`);
    next.classList.add("background");
    next.classList.add("animated");
    next.classList.add("tada");
    next.classList.add("infinite");
  }

  reset() {
    this.disabled = false;
    let next = document.getElementById(`${this.end}`);
    this.begin = undefined; this.end = undefined;
    next.classList.remove("background");
    next.classList.remove("animated");
    next.classList.remove("tada");
    next.classList.remove("infinite");
    next.classList.remove("bee");
  }
}
