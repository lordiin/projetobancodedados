import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IDenuncia } from '../denuncia.model';
import { DenunciaService } from '../service/denuncia.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './denuncia-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DenunciaDeleteDialogComponent {
  denuncia?: IDenuncia;

  constructor(protected denunciaService: DenunciaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.denunciaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
