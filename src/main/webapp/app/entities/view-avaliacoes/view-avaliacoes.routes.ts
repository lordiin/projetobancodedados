import { Routes } from '@angular/router';

import { ViewAvaliacoesComponent } from './list/view-avaliacoes.component';
import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

const viewAvaliacoesRoute: Routes = [
  {
    path: '',
    component: ViewAvaliacoesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default viewAvaliacoesRoute;
