import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../api.service';
import { Document, DocumentLine } from '../models';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  constructor(private apiService: ApiService) {}

  /**
   * Get all documents
   * @returns Observable of Document array
   */
  getAllDocuments(): Observable<Document[]> {
    return this.apiService.get<Document[]>('/api/documents');
  }

  /**
   * Get document by ID
   * @param id Document ID
   * @returns Observable of Document
   */
  getDocumentById(id: number): Observable<Document> {
    return this.apiService.get<Document>(`/api/documents/${id}`);
  }

  /**
   * Create a new document
   * @param document Document data
   * @returns Observable of created Document
   */
  createDocument(document: Omit<Document, 'id'>): Observable<Document> {
    return this.apiService.post<Document>('/api/documents', document);
  }

  /**
   * Update an existing document
   * @param id Document ID
   * @param document Document data
   * @returns Observable of updated Document
   */
  updateDocument(id: number, document: Partial<Document>): Observable<Document> {
    return this.apiService.put<Document>(`/api/documents/${id}`, document);
  }

  /**
   * Delete a document
   * @param id Document ID
   * @returns Observable of operation result
   */
  deleteDocument(id: number): Observable<any> {
    return this.apiService.delete<any>(`/api/documents/${id}`);
  }
}
