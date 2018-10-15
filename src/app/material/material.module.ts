import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatGridListModule } from "@angular/material";

@NgModule({
  imports: [
    CommonModule, MatGridListModule
  ],
  exports: [
    MatGridListModule
  ],
  declarations: []
})
export class MaterialModule { }
