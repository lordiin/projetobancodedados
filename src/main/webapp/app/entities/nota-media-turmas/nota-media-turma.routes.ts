import { Routes } from '@angular/router';

import { NotaMediaTurmaComponent } from './list/nota-media-turma.component';
import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

const notaMediaTurmasRoute: Routes = [
  {
    path: '',
    component: NotaMediaTurmaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notaMediaTurmasRoute;
