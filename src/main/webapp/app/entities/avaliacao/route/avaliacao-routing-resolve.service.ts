import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvaliacao } from '../avaliacao.model';
import { AvaliacaoService } from '../service/avaliacao.service';

export const avaliacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAvaliacao> => {
  const id = route.params['id'];
  const turmaId = route.params['turmaId'];
  if (id) {
    return inject(AvaliacaoService)
      .find(id)
      .pipe(
        mergeMap((avaliacao: HttpResponse<IAvaliacao>) => {
          if (avaliacao.body) {
            return of(avaliacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  if (turmaId) {
  }
  return of(null);
};

export default avaliacaoResolve;
