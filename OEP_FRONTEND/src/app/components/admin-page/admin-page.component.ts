import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { AdminService } from '../../services/admin/admin-service.service';
import { ExaminerService } from '../../services/examiner/examiner.service';
import { User } from '../../model/interfaces/user';
import { Exam } from '../../model/interfaces/exam';
import { FormsModule } from '@angular/forms';


interface UserRequest {
  userName: string;
  email: string;
  role: 'EXAMINER' | 'ADMIN' | 'STUDENT';
  password: string;
}

@Component({
  selector: 'app-admin-page',
  imports: [CommonModule,FormsModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})

export class AdminPageComponent {
  constructor(private authService: AuthService, private router: Router, private adminService: AdminService, private examinerService: ExaminerService) { }
  
  users: User[] = [];
  exams: Exam[] = [];
  selectedOption: string | null = null;

  registrationData: UserRequest = {
    userName: '',
    email: '',
    role: 'STUDENT',
    password: '',
  };
  registrationSuccessMessage: string = '';
  registrationErrorMessage: string = '';
  showSuccessPopup: boolean = false; 

  selectOption(option: string): void {
    this.selectedOption = option;
    // You can add logic here to load data or navigate based on the selected option
    console.log('Selected option:', option);

    if (this.selectedOption === 'users') {
      this.loadUsers();
    }
    else if (this.selectedOption === 'exams') {
      this.loadExams();
    }
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe(
      (data: User[]) => {
        this.users = data;
        console.log('Users loaded:', this.users);
      },
      (error) => {
        console.error('Error loading users:', error);
      }
    );
  }

  loadExams(): void {
    this.examinerService.getAllExams().subscribe( // Use the ExamService
      (data: Exam[]) => {
        this.exams = data;
        console.log('Exams loaded:', this.exams);
      },
      (error) => {
        console.error('Error loading exams:', error);
      }
    );
  }

  registerUser(): void {
    this.registrationSuccessMessage = '';
    this.registrationErrorMessage = '';

    this.authService.registerUser(this.registrationData).subscribe(
      (response) => {
        console.log('Registration successful:', response);
        this.showSuccessPopup = true; // Show the success popup
        // Optionally reset the form after a short delay
        setTimeout(() => {
          this.registrationData = { userName: '', email: '', role: 'STUDENT', password: '' };
          this.showSuccessPopup = false; // Hide the popup after reset
        }, 2000); // Adjust the delay as needed
      },
      (error) => {
        console.error('Registration failed:', error);
        this.registrationErrorMessage = error?.error?.message || 'Registration failed. Please try again.';
      }
    );
  }

  logout(): void {
    this.authService.removeToken(); // Remove the token from localStorage
    this.router.navigate(['/login']); // Redirect the user to the login page
  }
}
