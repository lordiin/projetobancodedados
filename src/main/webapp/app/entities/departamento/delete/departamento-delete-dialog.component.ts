import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IDepartamento } from '../departamento.model';
import { DepartamentoService } from '../service/departamento.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './departamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DepartamentoDeleteDialogComponent {
  departamento?: IDepartamento;

  constructor(protected departamentoService: DepartamentoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
