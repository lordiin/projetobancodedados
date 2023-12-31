import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TurmaComponent } from './list/turma.component';
import { TurmaDetailComponent } from './detail/turma-detail.component';
import { TurmaUpdateComponent } from './update/turma-update.component';
import TurmaResolve from './route/turma-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { TurmaAvaliacoesComponent } from './avaliacoes/turma-avaliacoes.component';
import turmaAvaliacoesResolve from './route/turma-avaliacoes-routing-resolve.service';

const turmaRoute: Routes = [
  {
    path: '',
    component: TurmaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TurmaDetailComponent,
    resolve: {
      turma: TurmaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TurmaUpdateComponent,
    resolve: {
      turma: TurmaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TurmaUpdateComponent,
    resolve: {
      turma: TurmaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/avaliacoes',
    component: TurmaAvaliacoesComponent,
    resolve: {
      turma: turmaAvaliacoesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default turmaRoute;
