import { Routes } from '@angular/router';

import { AbrirOcorrenciaPage } from './cad/pages/abrir-ocorrencia.page';
import { MesaPage } from './sala/pages/mesa.page';

export const routes: Routes = [
  { path: '', component: AbrirOcorrenciaPage },
  { path: 'cad', component: AbrirOcorrenciaPage },
  { path: 'sala', component: MesaPage },
];
