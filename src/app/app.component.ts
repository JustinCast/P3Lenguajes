import { Component, OnInit, Input } from '@angular/core';
import { trigger, state, style, animate, transition } from '@angular/animations';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('changeState', [
      state('state1', style({
        backgroundColor: 'green',
        transform: 'scale(1)'
      })),
      state('state2', style({
        backgroundColor: 'red',
        transform: 'scale(1.5)'
      })),
      transition('*=>state1', animate('300ms')),
      transition('*=>state2', animate('2000ms'))
    ])
  ]
})
export class AppComponent implements OnInit {
  toState = 'state1';
  values: Array<number> = [];
  title = 'P3Lenguajes';

  ngOnInit() {
    for (let i = 1; i <= 36; i++) {
      this.values.push(i);
    }
  }

  changeState(state: any) {
    this.toState = state;
    console.log(this.toState);
  }
}
