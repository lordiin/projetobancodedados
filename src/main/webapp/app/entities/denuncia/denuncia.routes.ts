import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DenunciaComponent } from './list/denuncia.component';
import { DenunciaDetailComponent } from './detail/denuncia-detail.component';
import { DenunciaUpdateComponent } from './update/denuncia-update.component';
import DenunciaResolve from './route/denuncia-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const denunciaRoute: Routes = [
  {
    path: '',
    component: DenunciaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DenunciaDetailComponent,
    resolve: {
      denuncia: DenunciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DenunciaUpdateComponent,
    resolve: {
      denuncia: DenunciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DenunciaUpdateComponent,
    resolve: {
      denuncia: DenunciaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default denunciaRoute;
