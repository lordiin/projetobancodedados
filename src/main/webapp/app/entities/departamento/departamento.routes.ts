import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepartamentoComponent } from './list/departamento.component';
import { DepartamentoDetailComponent } from './detail/departamento-detail.component';
import { DepartamentoUpdateComponent } from './update/departamento-update.component';
import DepartamentoResolve from './route/departamento-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const departamentoRoute: Routes = [
  {
    path: '',
    component: DepartamentoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepartamentoDetailComponent,
    resolve: {
      departamento: DepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepartamentoUpdateComponent,
    resolve: {
      departamento: DepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepartamentoUpdateComponent,
    resolve: {
      departamento: DepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default departamentoRoute;
