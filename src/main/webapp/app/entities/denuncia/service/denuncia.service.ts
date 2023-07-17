import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDenuncia, NewDenuncia } from '../denuncia.model';

export type PartialUpdateDenuncia = Partial<IDenuncia> & Pick<IDenuncia, 'id'>;

export type EntityResponseType = HttpResponse<IDenuncia>;
export type EntityArrayResponseType = HttpResponse<IDenuncia[]>;

@Injectable({ providedIn: 'root' })
export class DenunciaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/denuncias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(denuncia: NewDenuncia): Observable<EntityResponseType> {
    return this.http.post<IDenuncia>(this.resourceUrl, denuncia, { observe: 'response' });
  }

  createDenuncia(denuncia: NewDenuncia, avaliacaoId: number): Observable<EntityResponseType> {
    return this.http.post<IDenuncia>(`${this.resourceUrl}/avaliacao/${avaliacaoId}`, denuncia, { observe: 'response' });
  }

  update(denuncia: IDenuncia): Observable<EntityResponseType> {
    return this.http.put<IDenuncia>(`${this.resourceUrl}/${this.getDenunciaIdentifier(denuncia)}`, denuncia, { observe: 'response' });
  }

  partialUpdate(denuncia: PartialUpdateDenuncia): Observable<EntityResponseType> {
    return this.http.patch<IDenuncia>(`${this.resourceUrl}/${this.getDenunciaIdentifier(denuncia)}`, denuncia, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDenuncia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDenuncia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDenunciaIdentifier(denuncia: Pick<IDenuncia, 'id'>): number {
    return denuncia.id;
  }

  compareDenuncia(o1: Pick<IDenuncia, 'id'> | null, o2: Pick<IDenuncia, 'id'> | null): boolean {
    return o1 && o2 ? this.getDenunciaIdentifier(o1) === this.getDenunciaIdentifier(o2) : o1 === o2;
  }

  addDenunciaToCollectionIfMissing<Type extends Pick<IDenuncia, 'id'>>(
    denunciaCollection: Type[],
    ...denunciasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const denuncias: Type[] = denunciasToCheck.filter(isPresent);
    if (denuncias.length > 0) {
      const denunciaCollectionIdentifiers = denunciaCollection.map(denunciaItem => this.getDenunciaIdentifier(denunciaItem)!);
      const denunciasToAdd = denuncias.filter(denunciaItem => {
        const denunciaIdentifier = this.getDenunciaIdentifier(denunciaItem);
        if (denunciaCollectionIdentifiers.includes(denunciaIdentifier)) {
          return false;
        }
        denunciaCollectionIdentifiers.push(denunciaIdentifier);
        return true;
      });
      return [...denunciasToAdd, ...denunciaCollection];
    }
    return denunciaCollection;
  }
}
