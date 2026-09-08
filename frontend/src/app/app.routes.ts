import { Routes } from '@angular/router';

import { AbrirOcorrenciaPage } from './cad/pages/abrir-ocorrencia.page';

export const routes: Routes = [
  { path: '', component: AbrirOcorrenciaPage },
  { path: 'cad', component: AbrirOcorrenciaPage },
];
