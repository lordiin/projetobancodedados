import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDenuncia } from '../denuncia.model';
import { DenunciaService } from '../service/denuncia.service';

export const denunciaResolve = (route: ActivatedRouteSnapshot): Observable<null | IDenuncia> => {
  const id = route.params['id'];
  if (id) {
    return inject(DenunciaService)
      .find(id)
      .pipe(
        mergeMap((denuncia: HttpResponse<IDenuncia>) => {
          if (denuncia.body) {
            return of(denuncia.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default denunciaResolve;
