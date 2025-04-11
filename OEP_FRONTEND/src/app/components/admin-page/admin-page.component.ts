import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-page',
  imports: [CommonModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent {
  selectedOption: string | null = null;

  selectOption(option: string): void {
    this.selectedOption = option;
    // You can add logic here to load data or navigate based on the selected option
    console.log('Selected option:', option);
  }

}
