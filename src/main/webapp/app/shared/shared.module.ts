import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { ShowImageComponent } from './imagens/show-image.component';
import { ShowImageService } from './imagens/show-image.service';

/**
 * Application wide Module
 */
@NgModule({
  imports: [AlertComponent, AlertErrorComponent, CommonModule, FontAwesomeModule],
  exports: [CommonModule, NgbModule, FontAwesomeModule, AlertComponent, AlertErrorComponent, ShowImageComponent],
  declarations: [ShowImageComponent],
  providers: [ShowImageService],
})
export default class SharedModule {}
