import { Component, OnInit } from '@angular/core';
import { Observable, interval } from "rxjs";
import { MatGridTile } from '@angular/material';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
  ]
})
export class AppComponent implements OnInit {
  state: Array<string> = [];
  values: Array<number> = [];
  title = 'P3Lenguajes';
  interval;

  ngOnInit() {
    for (let i = 1; i <= 36; i++) {
      this.values.push(i);
    }
  }

  start(tile, index: number){
    console.log(tile)
    let count = index-1;
    this.interval = setInterval(() => {
      count++;
      if(count === this.values.length) {
        clearInterval(this.interval);
        return;
      };
      let style = document.createElement("style");
      style.type = "text/css";
      style.innerHTML =
      ".background { background-image: url('../assets/spider.png'); background-repeat: no-repeat}";
      document.getElementsByTagName("head")[0].appendChild(style);
      let prev = document.getElementById(`${count-1}`);
      prev.classList.remove("background")

      let next = document.getElementById(`${count}`);
      //console.log(next)
      next.classList.add("background");
    },1000)
  }
}
