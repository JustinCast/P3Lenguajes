import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  values: Array<number> = [];
  title = 'P3Lenguajes';

  ngOnInit() {
    for (let i = 1; i <= 36; i++) {
      this.values.push(i);
    }
  }
}
