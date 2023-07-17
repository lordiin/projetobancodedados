import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';
import { IAvaliacao } from '../../avaliacao/avaliacao.model';

export const turmaAvaliacoesResolve = (route: ActivatedRouteSnapshot): Observable<null | ITurma> => {
  const id = route.params['id'];
  if (id) {
    return inject(TurmaService)
      .find(id)
      .pipe(
        mergeMap((turma: HttpResponse<ITurma>) => {
          if (turma.body) {
            return of(turma.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default turmaAvaliacoesResolve;
