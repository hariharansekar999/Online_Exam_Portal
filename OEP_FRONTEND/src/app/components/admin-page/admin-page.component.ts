import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { AdminService } from '../../services/admin/admin-service.service';
import { ExaminerService } from '../../services/examiner/examiner.service';
import { User } from '../../model/interfaces/user';
import { Exam } from '../../model/interfaces/exam';
import { FormsModule } from '@angular/forms';
import { Report } from '../../model/interfaces/report'; // Import Report interface

interface UserRequest {
  userName: string;
  email: string;
  role: 'EXAMINER' | 'ADMIN' | 'STUDENT';
  password: string;
}
interface PasswordUpdateRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

@Component({
  selector: 'app-admin-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css'
})
export class AdminPageComponent implements OnInit {
  users: User[] = [];
  exams: Exam[] = [];
  reports: Report[] = [];
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


  constructor(
    private authService: AuthService,
    private router: Router,
    private adminService: AdminService,
    private examinerService: ExaminerService
  ) { }

  ngOnInit(): void {
    // Load initial data or set default view if needed
    this.selectOption(null); // Or a default option like 'users'
  }

  selectOption(option: string | null): void {
    this.selectedOption = option;
    console.log('Selected option:', option);

    if (this.selectedOption === 'users') {
      this.loadUsers();
    } else if (this.selectedOption === 'exams') {
      this.loadExams();
    } else if (this.selectedOption === 'reports') {
      this.loadReports();
    } else if (this.selectedOption === 'profileUpdate') {
      console.log('Profile update selected');
    }
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe(
      (data) => {
        this.users = data;
        console.log('Users loaded:', this.users);
      },
      (error) => {
        console.error('Error loading users:', error);
      }
    );
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

  loadReports(): void {
    this.adminService.getAllReports().subscribe(
      (data) => {
        this.reports = data;
        console.log('Reports loaded:', this.reports);
      },
      (error) => {
        console.error('Error loading reports:', error);
      }
    );
  }


  registerUser(): void {
    this.registrationSuccessMessage = '';
    this.registrationErrorMessage = '';

    this.authService.registerUser(this.registrationData).subscribe(
      (response) => {
        console.log('Registration successful:', response);
        this.showSuccessPopup = true;
        setTimeout(() => {
          this.registrationData = { userName: '', email: '', role: 'STUDENT', password: '' };
          this.showSuccessPopup = false;
        }, 2000);
      },
      (error) => {
        console.error('Registration failed:', error);
        this.registrationErrorMessage = error?.error?.message || 'Registration failed. Please try again.';
      }
    );
  }

  deleteUser(username: string): void {
    if (confirm(`Are you sure you want to delete user "${username}"?`)) {
      this.adminService.deleteUser(username).subscribe(
        (response) => {
          console.log('User deleted successfully:', response);
          this.loadUsers();
          alert(`User "${username}" deleted successfully.`);
        },
        (error) => {
          console.error('Error deleting user:', error);
          alert(`Error deleting user "${username}". Please try again.`);
        }
      );
    }
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }
}