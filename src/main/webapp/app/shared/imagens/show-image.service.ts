import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Imagem } from './imagem.model';

@Injectable()
export class ShowImageService {
  private resourceUrl = SERVER_API_URL;
  constructor(private http: HttpClient) {}

  getImageFile(url: string) {
    return this.http.get<Imagem>(`${this.resourceUrl}/${url}`, { observe: 'response' });
  }
}
