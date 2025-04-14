import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../../services/student/student.service';
import { ExamAnswersDTO } from '../../model/interfaces/exam-answers-dto';
import { ExamResponseDTO } from '../../model/interfaces/exam-response-dto';
import { CommonModule } from '@angular/common';
import { Question } from '../../model/interfaces/question';
import { Exam } from '../../model/interfaces/exam';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-attend-exam',
  templateUrl: './attend-exam.component.html',
  styleUrls: ['./attend-exam.component.css'],
  imports: [CommonModule, FormsModule]
})
export class AttendExamComponent implements OnInit {
  examId: number = 0;
  username: string = '';
  questions: Question[] = [];
  answers: { [questionId: number]: string } = {};
  timer: number = 0;
  interval: any;
  exams: Exam[] = [];

  showModal: boolean = false;
  modalTitle: string = '';
  modalMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private studentService: StudentService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.examId = +params['examId'];
    });

    this.route.queryParams.subscribe(params => {
      this.username = params['username']; // Get username from query params
    });

    console.log(this.username);
    this.loadData();
  }

  loadData(): void {
    this.studentService.getExams().subscribe(response => {
      this.exams = response.data || [];
      const exam = this.exams.find(ex => ex.examId === this.examId);
      if (exam) {
        this.questions = exam.questions || [];
        this.startTimer(exam); // Start timer after data is loaded
      }
    });
  }

  startTimer(exam: Exam): void {
    if (exam) {
      this.timer = exam.duration * 60;
      this.interval = setInterval(() => {
        this.timer--;
        if (this.timer <= 0) {
          clearInterval(this.interval);
          this.submitExam();
        }
      }, 1000);
    }
  }

  submitExam(): void {
    const examAnswers: ExamAnswersDTO[] = this.questions.map(question => ({
      questionId: question.questionId,
      answer: this.answers[question.questionId] || ''
    }));

    const examResponse: ExamResponseDTO = {
      examId: this.examId,
      userName: this.username,
      answers: examAnswers
    };

    console.log(this.username);

    this.studentService.submitResponse(examResponse).subscribe(
      (response) => {
        this.openModal('Success', 'Exam submitted successfully.');
      },
      (error) => {
        this.openModal('Error', 'Error submitting exam.');
      }
    );
  }

  openModal(title: string, message: string): void {
    this.modalTitle = title;
    this.modalMessage = message;
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.router.navigate(['/student']);
  }
}