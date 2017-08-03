import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ScriptengineApiScriptModule } from './script/script.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ScriptengineApiScriptModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ScriptengineApiEntityModule {}
