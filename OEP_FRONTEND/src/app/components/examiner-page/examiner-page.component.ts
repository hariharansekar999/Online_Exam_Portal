import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ExaminerService } from '../../services/examiner/examiner.service';
import { Exam } from '../../model/interfaces/exam';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { FormsModule } from '@angular/forms';

interface ExamDTO {
  title: string;
  description?: string;
  totalMarks: number | null;
  duration: number | null;
}

@Component({
  selector: 'app-examiner-page',
  imports: [CommonModule,FormsModule],
  templateUrl: './examiner-page.component.html',
  styleUrl: './examiner-page.component.css'
})
export class ExaminerPageComponent {
  selectedOption: string | null = null;

  examData: ExamDTO = {
    title: '',
    description: '',
    totalMarks: null,
    duration: null
  };
  category: string = ''; 
  errorMessage: string = '';
  successMessage: string = '';
  messageTimeout: any; // To hold the timeout ID

  constructor(private examinerService: ExaminerService, private router: Router, private authService: AuthService) { }
  exams: Exam[] = [];

  selectOption(option: string | null): void {
    this.selectedOption = option;
    console.log('Selected option:', option);

    if (this.selectedOption === 'users') {
      console.log('Users selected');
    } else if (this.selectedOption === 'exams') {
      this.loadExams();
    } else if (this.selectedOption === 'reports') {
      console.log('Reports selected');
    } else if (this.selectedOption === 'updatePassword') {
      console.log('Profile update selected');
    } else if (this.selectedOption === 'logout') {
      this.logout();
    }
  }

  loadExams(): void {
    this.examinerService.getAllExams().subscribe(
      (data) => {
        this.exams = data;
        console.log('Exams loaded:', this.exams);
      },
      (error) => {
        console.error('Error loading exams:', error);
      }
    );
  }

  clearMessages(): void {
    clearTimeout(this.messageTimeout);
    this.messageTimeout = null;
    this.errorMessage = '';
    this.successMessage = '';
  }

  createExam(): void {
    this.clearMessages(); // Clear any existing messages

    if (!this.category) {
      this.errorMessage = 'Error: Exam category is required.';
      this.setMessageTimeout();
      return;
    }

    this.examinerService.createExam(this.category, this.examData).subscribe(
      (response) => {
        if (response.success && response.data) {
          this.successMessage = 'Exam created successfully!';
          this.examData = { title: '', description: '', totalMarks: null, duration: null };
          this.category = '';
          this.setMessageTimeout(); // Set timeout for success message
        } else {
          this.errorMessage = response.errorMessage || 'Failed to create exam.';
          this.setMessageTimeout(); // Set timeout for error message
        }
      },
      (error) => {
        console.error('Error creating exam:', error);
        this.errorMessage = 'An unexpected error occurred while creating the exam.';
        this.setMessageTimeout(); // Set timeout for error message
      }
    );
  }

  // Helper function to set a timeout for messages
  setMessageTimeout(): void {
    this.messageTimeout = setTimeout(() => {
      this.clearMessages();
    }, 3000); // Adjust the delay (in milliseconds) as needed
  }

  deleteExam(examId: number): void {
    this.clearMessages(); // Clear any existing messages

    this.examinerService.deleteExam(examId).subscribe(
      (response) => {
        if (response.success && response.data) {
          this.successMessage = 'Exam deleted successfully!';
          this.loadExams(); // Reload exams after deletion
          this.setMessageTimeout(); // Set timeout for success message
        } else {
          this.errorMessage = response.errorMessage || 'Failed to delete exam.';
          this.setMessageTimeout(); // Set timeout for error message
        }
      },
      (error) => {
        console.error('Error deleting exam:', error);
        this.errorMessage = 'An unexpected error occurred while deleting the exam.';
        this.setMessageTimeout(); // Set timeout for error message
      }
    );
  }
  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

}
