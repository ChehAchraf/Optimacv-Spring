import {Component, inject} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  UploadCloud,
  FileText,
} from 'lucide-angular';
import {resumeStore} from '../../../../../../core/store/resume.store';
import {toast} from 'ngx-sonner';

@Component({
  selector: 'app-cv-upload-zone-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './cv-upload-zone-component.html',
})
export class CvUploadZoneComponent {

  private readonly store = inject(resumeStore)

  readonly UploadIcon = UploadCloud;
  readonly FileTextIcon = FileText;

  onFileSelected(event: Event) {

    const input = event.target as HTMLInputElement

    if(input.files && input.files.length > 0){
      const file = input.files[0]

      if(file.type !== 'application/pdf'){
        toast.error("please chose only pdf!")
        input.value = ''
        return
      }

      const maxSize = 5 * 1024 * 1024
      if(file.size > maxSize){
        toast.error("the file is too large, the max size is 5 Mb");
        input.value=""
        return
      }
      this.store.uploadResume({file:file});
      input.value = ""
    }

  }
}

