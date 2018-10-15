import { Component, OnInit } from '@angular/core';
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
  enabled: boolean = false;
  begin: number;
  end: number;

  ngOnInit() {
    for (let i = 1; i <= 36; i++) {
      this.values.push(i);
    }
  }

  selectParams(index: number) {
    if(!this.begin)
      this.begin = index -1;
    else{
      this.end = index + 1;
      this.enabled = true;
      this.start();
    }
  }

  start(){
    this.interval = setInterval(() => {
      this.begin++;
      if(this.begin === this.end) {
        clearInterval(this.interval);
        return;
      };
      let style = document.createElement("style");
      style.type = "text/css";
      style.innerHTML =
      ".background { background-image: url('../assets/spider.png'); background-repeat: no-repeat}";
      document.getElementsByTagName("head")[0].appendChild(style);
      let prev = document.getElementById(`${this.begin-1}`);
      prev.classList.remove("background")

      let next = document.getElementById(`${this.begin}`);
      //console.log(next)
      next.classList.add("background");
    },1000)
  }
}
