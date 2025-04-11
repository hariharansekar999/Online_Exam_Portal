import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-examiner-page',
  imports: [CommonModule],
  templateUrl: './examiner-page.component.html',
  styleUrl: './examiner-page.component.css'
})
export class ExaminerPageComponent {
  selectedOption: string | null = null;

  selectOption(option: string): void {
    this.selectedOption = option;
    // You can add logic here to load data or navigate based on the selected option
    console.log('Selected option:', option);
  }
}
