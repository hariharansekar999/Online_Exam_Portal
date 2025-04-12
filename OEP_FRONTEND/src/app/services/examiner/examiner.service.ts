import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Exam } from '../../model/interfaces/exam'; // Adjust the path

interface ApiResponse {
  success: boolean;
  status: string;
  data: any[]; // Change 'Exam[]' to 'any[]' initially
  errorMessage: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class ExaminerService {

  http = inject(HttpClient);
  private baseUrl = 'http://localhost:8090/examiner';

  constructor() { }

  getAllExams(): Observable<Exam[]> {
    return this.http.get<Exam[]>(this.baseUrl + '/allExams').pipe(
      map(exams => {
        console.log('Exams fetched successfully:', exams);
        return exams;
      })
    );
  }

}