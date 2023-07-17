import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { INotaMediaTurma } from '../nota-media-turma.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { AvaliacaoService } from '../../avaliacao/service/avaliacao.service';

@Component({
  standalone: true,
  selector: 'jhi-avaliacao',
  templateUrl: './nota-media-turma.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    HasAnyAuthorityDirective,
  ],
})
export class NotaMediaTurmaComponent implements OnInit {
  notaMediaTurmas?: INotaMediaTurma[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected avaliacaoService: AvaliacaoService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.avaliacaoService.getNotaMediaTurmas().subscribe(res => {
      this.notaMediaTurmas = res.body;
    });
  }
}
