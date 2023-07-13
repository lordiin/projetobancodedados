import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DisciplinaComponent } from './list/disciplina.component';
import { DisciplinaDetailComponent } from './detail/disciplina-detail.component';
import { DisciplinaUpdateComponent } from './update/disciplina-update.component';
import DisciplinaResolve from './route/disciplina-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const disciplinaRoute: Routes = [
  {
    path: '',
    component: DisciplinaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisciplinaDetailComponent,
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisciplinaUpdateComponent,
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisciplinaUpdateComponent,
    resolve: {
      disciplina: DisciplinaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disciplinaRoute;
