import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ScriptengineApiSharedModule } from '../../shared';
import { ScriptengineApiAdminModule } from '../../admin/admin.module';
import {
    ScriptService,
    ScriptPopupService,
    ScriptComponent,
    ScriptDetailComponent,
    ScriptDialogComponent,
    ScriptPopupComponent,
    ScriptDeletePopupComponent,
    ScriptDeleteDialogComponent,
    scriptRoute,
    scriptPopupRoute,
    ScriptResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...scriptRoute,
    ...scriptPopupRoute,
];

@NgModule({
    imports: [
        ScriptengineApiSharedModule,
        ScriptengineApiAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ScriptComponent,
        ScriptDetailComponent,
        ScriptDialogComponent,
        ScriptDeleteDialogComponent,
        ScriptPopupComponent,
        ScriptDeletePopupComponent,
    ],
    entryComponents: [
        ScriptComponent,
        ScriptDialogComponent,
        ScriptPopupComponent,
        ScriptDeleteDialogComponent,
        ScriptDeletePopupComponent,
    ],
    providers: [
        ScriptService,
        ScriptPopupService,
        ScriptResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptengineApiScriptModule {}
