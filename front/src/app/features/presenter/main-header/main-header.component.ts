import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-main-header',
  imports: [],
  templateUrl: './main-header.component.html',
  styleUrl: './main-header.component.css'
})
export class MainHeaderComponent {
  @Input() title: string = '';
  @Input() icon: string = '';

}
