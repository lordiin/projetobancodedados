import { Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { ShowImageService } from './show-image.service';
import { Imagem } from './imagem.model';
import { HttpResponse } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-show-image',
  templateUrl: './show-image.component.html',
  styleUrls: ['show-image.scss'],
})
export class ShowImageComponent implements OnInit, OnChanges {
  @Input() inputName: any;
  @Input() url: any;
  @Input() titulo: any;
  @Input() width: any;
  @Input() justifyContent: any;
  @Input() height: any;
  @Input() buttonRight: any;
  @Input() downloadable: any;
  @Input() doNotView: any;
  @Input() editable: any;
  @Input() imagem?: Imagem;
  @Input() loading = false;
  @Output() onChange? = new EventEmitter<Imagem>();
  // @ts-ignore
  @ViewChild('inputFile') inputFile: ElementRef;
  newTab = true;

  constructor(private showImageService: ShowImageService, private _sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    if (this.url) {
      this.showImageService.getImageFile(this.url).subscribe(
        (res: HttpResponse<Imagem>) => {
          this.imagem = res.body ?? undefined;
          this.imagem.imageToShow = this._sanitizer.bypassSecurityTrustResourceUrl(this.imagem?.contentType + ',' + this.imagem?.arquivo);
          this.onChange.emit(this.imagem);
          this.loading = false;
        },
        () => (this.loading = false)
      );
    }
    if (!this.width) {
      this.width = '100%';
    }
    if (!this.height) {
      this.height = 'auto';
    }
    if (this.doNotView) {
      this.newTab = false;
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.imagem && changes.imagem.currentValue) {
      this.imagem = changes.imagem.currentValue;
      this.refreshImage(this.imagem);
    }
  }

  refreshImage(image: Imagem) {
    this.imagem = image;
    if (image && image.arquivo) {
      this.imagem.imageToShow = this._sanitizer.bypassSecurityTrustResourceUrl(this.getContentType() + this.imagem.arquivo);
    }
  }

  private getContentType(): string {
    if (this.imagem.contentType && this.imagem.contentType.includes(';base64')) {
      return this.imagem.contentType + ',';
    }
    return 'data:' + this.imagem.contentType + ';base64' + ',';
  }

  clearFile() {
    this.imagem.arquivo = undefined;
    this.imagem.contentType = undefined;
    this.imagem.imageToShow = undefined;
    this.inputFile.nativeElement.value = '';
    this.onChange.emit(this.imagem);
  }

  editFile(event: any) {
    const fileReader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      if (!this.imagem) {
        this.imagem = new Imagem();
      }
      const file = event.target.files[0];
      fileReader.onload = () => {
        this.imagem.contentType = (<string>fileReader.result).split(',')[0];
        this.imagem.arquivo = (<string>fileReader.result).split(',')[1];
        this.refreshImage(this.imagem);
        this.onChange.emit(this.imagem);
      };
      fileReader.readAsDataURL(file);
    }
  }

  getInputId() {
    return this.inputName + 'id';
  }

  isPdf(contentType: string) {
    return contentType && contentType.includes('pdf');
  }
}
