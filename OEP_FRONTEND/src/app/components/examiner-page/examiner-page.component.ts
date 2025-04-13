import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { QuestionService } from '../../services/qurestion/question.service';
import { QuestionDTO } from '../../model/interfaces/question-dto';
import { ExaminerService } from '../../services/examiner/examiner.service'; // Import ExaminerService
import { Exam } from '../../model/interfaces/exam'; // Import Exam interface
import { Question } from '../../model/interfaces/question';
import { ApiError } from '../../model/interfaces/apierror';
import { HttpErrorResponse } from '@angular/common/http';
import { Report } from '../../model/interfaces/report';
import { AdminService } from '../../services/admin/admin-service.service';

interface ApiResponse<T> {
  success: boolean;
  status: string;
  data: T | null;
  errorMessage: string | null;
}

interface ExamDTO {
  title: string;
  description?: string;
  totalMarks: number | null;
  duration: number | null;
}

interface CategoryQuestionDTO {
  description: string;
  correctAnswer: string;
  difficulty: string;
}

@Component({
  selector: 'app-examiner-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './examiner-page.component.html',
  styleUrl: './examiner-page.component.css'
})
export class ExaminerPageComponent implements OnInit {
  selectedOption: string | null = null;
  allQuestions: Question[] = [];

  examData: ExamDTO = {
    title: '',
    description: '',
    totalMarks: null,
    duration: null
  };
  category: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  messageTimeout: any;
  exams: Exam[] = [];

  showSingleQuestionForm: boolean = false;
  showMultipleQuestionsForm: boolean = false;
  isShowCreateByCategoryForm: boolean = false; // Corrected property name

  newQuestion: QuestionDTO = {
    description: '',
    category: '',
    difficulty: '',
    correctAnswer: ''
  };

  multipleQuestions: QuestionDTO[] = [];
  currentMultipleQuestion: QuestionDTO = {
    description: '',
    category: '',
    difficulty: '',
    correctAnswer: ''
  };

  categoryQuestions: CategoryQuestionDTO[] = [];
  currentCategoryQuestion: CategoryQuestionDTO = {
    description: '',
    correctAnswer: '',
    difficulty: ''
  };
  selectedCategoryForQuestions: string = '';

  showUpdateExamForm: boolean = false;
  showUpdateQuestionForm: boolean = false;

  selectedExamIdForUpdate: number | null = null;
  updateExamData: ExamDTO = { title: '', description: '', totalMarks: null, duration: null };

  selectedQuestionIdForUpdate: number | null = null;
  updateQuestionData: QuestionDTO = { description: '', category: '', difficulty: '', correctAnswer: '' };

  selectedExamIdForEvaluation: number | null = null;
  usernameToEvaluateSingle: string = '';
  evaluationMessage: string = '';
  isEvaluationSuccess: boolean | null = null;
  singleEvaluationResult: Report | null = null;
  fetchedExam: Exam | null = null;

  constructor(
    private examinerService: ExaminerService,
    private questionService: QuestionService,
    private router: Router,
    private authService: AuthService,
    private adminService: AdminService
  ) { }

  ngOnInit(): void {
    // Optionally load exams on initialization
    this.loadExams();
  }

  selectOption(option: string | null): void {
    this.selectedOption = option;
    this.clearMessages();
    this.showSingleQuestionForm = false;
    this.showMultipleQuestionsForm = false;
    this.isShowCreateByCategoryForm = false;
    if (option === 'createExams') {
      this.examData = { title: '', description: '', totalMarks: null, duration: null };
      this.category = '';
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;
    } else if (option === 'createQuestions') {
      this.newQuestion = { description: '', category: '', difficulty: '', correctAnswer: '' };
      this.multipleQuestions = [];
      this.currentMultipleQuestion = { description: '', category: '', difficulty: '', correctAnswer: '' };
      this.categoryQuestions = [];
      this.currentCategoryQuestion = { description: '', correctAnswer: '', difficulty: '' };
      this.selectedCategoryForQuestions = '';
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;
    } else if( option === 'viewQuestions') {
      this.loadAllQuestions();
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;
    }else if (option === 'updateData') {
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;
    } else if (option === 'updateExamForm') {
      this.showUpdateExamForm = true;
      this.showUpdateQuestionForm = false;
      this.loadExams();
    } else if (option === 'updateQuestionForm') {
      this.showUpdateQuestionForm = true;
      this.showUpdateExamForm = false;
      this.loadAllQuestions();
    } else if ( option === 'evaluateExams'){
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;

      this.selectedExamIdForEvaluation = null;
      this.usernameToEvaluateSingle = '';
      this.evaluationMessage = '';
      this.isEvaluationSuccess = null;
      this.singleEvaluationResult = null;
      this.fetchedExam = null;
    } else if( option === 'viewReports') {
      this.showAllReports();
      this.showUpdateExamForm = false;
      this.showUpdateQuestionForm = false;
    }
  }

  clearMessages(): void {
    clearTimeout(this.messageTimeout);
    this.messageTimeout = null;
    this.errorMessage = '';
    this.successMessage = '';
  }

  setMessageTimeout(): void {
    this.messageTimeout = setTimeout(() => {
      this.clearMessages();
    }, 3000);
  }

  // --- Exam related functions ---
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

  createExam(): void {
    this.clearMessages();
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
          this.setMessageTimeout();
          this.loadExams(); // Reload exams after creation
        } else {
          this.errorMessage = response.errorMessage || 'Failed to create exam.';
          this.setMessageTimeout();
        }
      },
      (error) => {
        console.error('Error creating exam:', error);
        this.errorMessage = 'An unexpected error occurred while creating the exam.';
        this.setMessageTimeout();
      }
    );
  }

  deleteExam(examId: number): void {
    this.clearMessages();
    this.examinerService.deleteExam(examId).subscribe(
      (response) => {
        if (response.success && response.data) {
          this.successMessage = 'Exam deleted successfully!';
          this.loadExams();
          this.setMessageTimeout();
        } else {
          this.errorMessage = response.errorMessage || 'Failed to delete exam.';
          this.setMessageTimeout();
        }
      },
      (error) => {
        console.error('Error deleting exam:', error);
        this.errorMessage = 'An unexpected error occurred while deleting the exam.';
        this.setMessageTimeout();
      }
    );
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/login']);
  }

  // --- Question form visibility control ---
  showCreateSingleQuestionForm(show: boolean = true): void {
    this.showSingleQuestionForm = show;
    this.showMultipleQuestionsForm = false;
    this.isShowCreateByCategoryForm = false;
  }

  showCreateMultipleQuestionsForm(show: boolean = true): void {
    this.showMultipleQuestionsForm = show;
    this.showSingleQuestionForm = false;
    this.isShowCreateByCategoryForm = false;
  }

  showCreateByCategoryForm(show: boolean = true): void {
    this.isShowCreateByCategoryForm = show;
    this.showSingleQuestionForm = false;
    this.showMultipleQuestionsForm = false;
  }

  removeMultipleQuestion(index: number): void {
    this.multipleQuestions.splice(index, 1);
  }
  // --- Question creation functions ---
  createSingleQuestion(): void {
    this.clearMessages();
    this.questionService.createQuestion(this.newQuestion).subscribe(
      (response) => {
        if (response.success && response.data) {
          this.successMessage = 'Question created successfully!';
          this.newQuestion = { description: '', category: '', difficulty: '', correctAnswer: '' };
          this.showCreateSingleQuestionForm(false);
          this.setMessageTimeout();
        } else {
          this.errorMessage = response.errorMessage || 'Failed to create question.';
          this.setMessageTimeout();
        }
      },
      (error) => {
        console.error('Error creating question:', error);
        this.errorMessage = 'An unexpected error occurred while creating the question.';
        this.setMessageTimeout();
      }
    );
  }

  addMultipleQuestion(): void {
    this.multipleQuestions.push({ ...this.currentMultipleQuestion, category: this.currentMultipleQuestion.category || 'default' });
    this.currentMultipleQuestion = { description: '', category: '', difficulty: '', correctAnswer: '' };
  }

  createMultipleQuestions(): void {
    this.clearMessages();
    if (this.multipleQuestions.length > 0) {
      this.questionService.createMultipleQuestions(this.multipleQuestions).subscribe(
        (response) => {
          if (response.success) {
            this.successMessage = 'Multiple questions created successfully!';
            this.multipleQuestions = [];
            this.showCreateMultipleQuestionsForm(false);
            this.setMessageTimeout();
          } else {
            this.errorMessage = response.errorMessage || 'Failed to create multiple questions.';
            this.setMessageTimeout();
          }
        },
        (error) => {
          console.error('Error creating multiple questions:', error);
          this.errorMessage = 'An unexpected error occurred while creating multiple questions.';
          this.setMessageTimeout();
        }
      );
    } else {
      this.errorMessage = 'Please add at least one question to the list.';
      this.setMessageTimeout();
    }
  }

  addCategoryQuestion(): void {
    if (this.selectedCategoryForQuestions) {
      this.categoryQuestions.push({ ...this.currentCategoryQuestion });
      this.currentCategoryQuestion = { description: '', correctAnswer: '', difficulty: '' };
    } else {
      this.errorMessage = 'Please enter a category.';
      this.setMessageTimeout();
    }
  }
  removeCategoryQuestion(index: number): void {
    this.categoryQuestions.splice(index, 1);
  }

  createByCategory(): void {
    this.clearMessages();
    if (!this.selectedCategoryForQuestions) {
      this.errorMessage = 'Please select a category for these questions.';
      this.setMessageTimeout();
      return;
    }
    if (this.categoryQuestions.length > 0) {
      const questionsToSend: QuestionDTO[] = this.categoryQuestions.map(q => ({
        description: q.description,
        correctAnswer: q.correctAnswer,
        difficulty: q.difficulty,
        category: this.selectedCategoryForQuestions
      }));
      this.questionService.createByCategory(this.selectedCategoryForQuestions, questionsToSend).subscribe(
        (response) => {
          if (response.success) {
            this.successMessage = `Questions for category "${this.selectedCategoryForQuestions}" added successfully!`;
            this.categoryQuestions = [];
            this.selectedCategoryForQuestions = '';
            this.showCreateByCategoryForm(false);
            this.setMessageTimeout();
          } else {
            this.errorMessage = response.errorMessage || `Failed to add questions for category "${this.selectedCategoryForQuestions}".`;
            this.setMessageTimeout();
          }
        },
        (error) => {
          console.error(`Error creating questions by category "${this.selectedCategoryForQuestions}":`, error);
          this.errorMessage = 'An unexpected error occurred while creating questions by category.';
          this.setMessageTimeout();
        }
      );
    } else {
      this.errorMessage = 'Please add at least one question to the list for this category.';
      this.setMessageTimeout();
    }
  }

  loadAllQuestions(): void {
    this.questionService.getAllQuestions().subscribe(
      (questions: Question[]) => { // Rename 'data' to 'questions' for clarity
        this.allQuestions = questions || []; // Directly assign the received array
        console.log('All questions loaded:', this.allQuestions);
      },
      (error: ApiError) => {
        console.error('Error loading all questions:', error);
        this.errorMessage = 'Failed to load all questions.';
        this.setMessageTimeout();
      }
    );
  }

  deleteQuestion(questionId: number): void {
    this.clearMessages();
    if (confirm('Are you sure you want to delete this question?')) {
      this.questionService.deleteQuestion(questionId).subscribe(
        (response: ApiResponse<any>) => {
          if (response.success) {
            this.successMessage = 'Question deleted successfully!';
            this.loadAllQuestions(); // Reload questions after deletion
            this.setMessageTimeout();
          } else {
            this.errorMessage = response.errorMessage || 'Failed to delete question.';
            this.setMessageTimeout();
          }
        },
        (error: ApiError) => {
          console.error('Error deleting question:', error);
          this.errorMessage = 'An unexpected error occurred while deleting the question.';
          this.setMessageTimeout();
        }
      );
    }
  }


  updateExamMessage: string = '';
  isUpdateExamSuccess: boolean = false;
  updateQuestionMessage: string = '';
  isUpdateQuestionSuccess: boolean = false;
  
  updateExam(): void {
    this.clearMessages();
    if (this.selectedExamIdForUpdate) {
      this.questionService.updateExam(this.selectedExamIdForUpdate, this.updateExamData).subscribe({
        next: (response: ApiResponse<Exam>) => {
          if (response.success && response.data) {
            this.updateExamMessage = `Exam with ID ${this.selectedExamIdForUpdate} updated successfully!`;
            this.isUpdateExamSuccess = true;
            this.loadExams();
            this.showUpdateExamForm = false;
            this.selectedOption = 'updateData';
            // No need for setMessageTimeout if using the modal
          } else {
            this.updateExamMessage = response.errorMessage || `Failed to update exam with ID ${this.selectedExamIdForUpdate}.`;
            this.isUpdateExamSuccess = false;
            // No need for setMessageTimeout if using the modal
          }
        },
        error: (error: ApiError) => {
          console.error(`Error updating exam with ID ${this.selectedExamIdForUpdate}:`, error);
          this.updateExamMessage = `An unexpected error occurred while updating exam with ID ${this.selectedExamIdForUpdate}.`;
          this.isUpdateExamSuccess = false;
          // No need for setMessageTimeout if using the modal
        }
      });
    } else {
      this.updateExamMessage = 'Please select an exam to update.';
      this.isUpdateExamSuccess = false;
      // No need for setMessageTimeout if using the modal
    }
  }


  updateQuestion(): void {
    this.clearMessages();
    if (this.selectedQuestionIdForUpdate) {
      this.questionService.updateQuestion(this.selectedQuestionIdForUpdate, this.updateQuestionData).subscribe({
        next: (response: ApiResponse<Question>) => {
          if (response.success && response.data) {
            this.updateQuestionMessage = `Question with ID ${this.selectedQuestionIdForUpdate} updated successfully!`;
            this.isUpdateQuestionSuccess = true;
            this.loadAllQuestions();
            this.showUpdateQuestionForm = false;
            this.selectedOption = 'updateData';
            this.updateQuestionData = { description: '', category: '', difficulty: '', correctAnswer: '' }; // Reset the form
            // No need for setMessageTimeout if using the modal
          } else {
            this.updateQuestionMessage = response.errorMessage || `Failed to update question with ID ${this.selectedQuestionIdForUpdate}.`;
            this.isUpdateQuestionSuccess = false;
            // No need for setMessageTimeout if using the modal
          }
        },
        error: (error: ApiError) => {
          console.error(`Error updating question with ID ${this.selectedQuestionIdForUpdate}:`, error);
          this.updateQuestionMessage = `An unexpected error occurred while updating question with ID ${this.selectedQuestionIdForUpdate}.`;
          this.isUpdateQuestionSuccess = false;
          // No need for setMessageTimeout if using the modal
        }
      });
    } else {
      this.updateQuestionMessage = 'Please select a question to update.';
      this.isUpdateQuestionSuccess = false;
      // No need for setMessageTimeout if using the modal
    }
  }

  closeUpdateExamModal() {
    this.updateExamMessage = '';
  }

  closeUpdateQuestionModal() {
    this.updateQuestionMessage = '';
  }

  evaluateSingleExam(): void {
    if (!this.selectedExamIdForEvaluation) {
      this.evaluationMessage = 'Please enter an Exam ID to evaluate.';
      this.isEvaluationSuccess = false;
      return;
    }
    if (!this.usernameToEvaluateSingle) {
      this.evaluationMessage = 'Please enter a username to evaluate.';
      this.isEvaluationSuccess = false;
      return;
    }

    this.evaluationMessage = 'Evaluating exam...';
    this.isEvaluationSuccess = null;
    this.singleEvaluationResult = null;
    this.fetchedExam = null;

    // Now we know selectedExamIdForEvaluation is not null due to the check above
    this.examinerService.evaluateExam(this.selectedExamIdForEvaluation, this.usernameToEvaluateSingle).subscribe({
      next: (response: ApiResponse<Report>) => {
        if (response.success && response.data) {
          this.singleEvaluationResult = response.data;
          this.evaluationMessage = `Evaluation for Exam ID ${this.selectedExamIdForEvaluation} and User "${this.usernameToEvaluateSingle}" successful!`;
          this.isEvaluationSuccess = true;
          this.fetchExamDetails(this.selectedExamIdForEvaluation!); // Use the non-null assertion operator
          this.selectedExamIdForEvaluation = null;
          this.usernameToEvaluateSingle = '';
        } else {
          this.evaluationMessage = response.errorMessage || `Failed to evaluate Exam ID ${this.selectedExamIdForEvaluation} for User "${this.usernameToEvaluateSingle}".`;
          this.isEvaluationSuccess = false;
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error(`Error evaluating Exam ID ${this.selectedExamIdForEvaluation} for User "${this.usernameToEvaluateSingle}":`, error);
        this.evaluationMessage = `An unexpected error occurred while evaluating Exam ID ${this.selectedExamIdForEvaluation} for User "${this.usernameToEvaluateSingle}".`;
        this.isEvaluationSuccess = false;
      }
    });
  }

  fetchExamDetails(examId: number): void {
    this.examinerService.getExamById(examId).subscribe({
      next: (response: ApiResponse<Exam>) => {
        if (response.success && response.data) {
          this.fetchedExam = response.data;
        } else {
          console.error(`Failed to load exam details for ID ${examId}:`, response.errorMessage);
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error(`Error loading exam details for ID ${examId}:`, error);
      }
    });
  }

  allReports: Report[] = [];
  showReportsList: boolean = false;
  selectedReportForFeedback: Report | null = null;
  feedbackToUpdate: string = '';
  updateFeedbackMessage: string = '';
  isUpdateFeedbackSuccess: boolean | null = null;

  updateReportFeedback(): void {
    if (!this.selectedReportForFeedback) {
      this.updateFeedbackMessage = 'Error: No report selected for feedback update.';
      this.isUpdateFeedbackSuccess = false;
      return;
    }
    if (!this.feedbackToUpdate.trim()) {
      this.updateFeedbackMessage = 'Error: Feedback cannot be empty.';
      this.isUpdateFeedbackSuccess = false;
      return;
    }

    this.updateFeedbackMessage = 'Updating feedback...';
    this.isUpdateFeedbackSuccess = null;

    // Add a check to ensure selectedReportForFeedback is not null before accessing properties
    if (this.selectedReportForFeedback) {
      this.examinerService.updateFeedback(
        this.selectedReportForFeedback.userName!, // Use non-null assertion here as we've checked above
        this.selectedReportForFeedback.examId,
        this.feedbackToUpdate
      ).subscribe({
        next: (response: ApiResponse<any>) => {
          if (response.success) {
            this.updateFeedbackMessage = `Feedback updated successfully for User "${this.selectedReportForFeedback!.userName}" and Exam ID ${this.selectedReportForFeedback!.examId}.`;
            this.isUpdateFeedbackSuccess = true;
            this.showAllReports();
            this.selectedReportForFeedback = null;
            this.feedbackToUpdate = '';
          } else {
            this.updateFeedbackMessage = response.errorMessage || 'Failed to update feedback.';
            this.isUpdateFeedbackSuccess = false;
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error updating feedback:', error);
          this.updateFeedbackMessage = 'An unexpected error occurred while updating feedback.';
          this.isUpdateFeedbackSuccess = false;
        }
      });
    }
  }

  reports: Report[] = [];
  showAllReports(): void {
    this.selectedOption = 'viewReports';
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

  openUpdateFeedbackForm(report: Report): void {
    this.selectedReportForFeedback = report;
    this.feedbackToUpdate = report.feedback || '';
    this.updateFeedbackMessage = '';
    this.isUpdateFeedbackSuccess = null;
  }
}