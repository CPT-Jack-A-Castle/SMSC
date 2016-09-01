import { Component } from "@angular/core";
import { TranslatePipe, TranslateService } from "ng2-translate/ng2-translate";
import { AgGridNg2 } from "ag-grid-ng2/main";
import { Router, ActivatedRoute } from "@angular/router";
import { CrudService } from "../crud.service";
import { AlertComponent } from "ng2-bootstrap/ng2-bootstrap";
import { LoadingGrid } from "../../common/loadingGrid";
import { Location } from "@angular/common";
import { GridPagination } from "../directives/gridPagination/gridPagination";
import { GridService } from "../../services/grid.service";

@Component({
    selector: 'crud-linkset',
    template: require('./crud.linkset.html'),
    styles: [
        require('../common/grid.scss'),
        require('../common/style.scss')
    ],
    providers: [],
    directives: [
        AgGridNg2,
        AlertComponent,
        LoadingGrid,
        GridPagination
    ],
    pipes: [TranslatePipe]
})

export class CrudLinkset {
    public resolveData: any;

    constructor(public translate: TranslateService,
                public crudService: CrudService,
                public router: Router,
                public route: ActivatedRoute,
                public location: Location,
                public gridService: GridService) {
    }

    ngOnInit() {
        this.resolveData = this.route.snapshot.data[0];

        this.crudService.gridOptions.columnDefs = this.resolveData;
    }

    back() {
        this.location.back();
    }

    addLink(gridOptions) {
        let params: any = this.crudService.modifiedRecord.data;

        return this.getLinkset(gridOptions)
            .then(linkSet => {
                if (this.crudService.modifiedRecord.type === 'LINK') {
                    params[this.crudService.modifiedRecord.modifiedLinkset] = linkSet.join().replace(/\[|\]/gi, '');
                } else {
                    params[this.crudService.modifiedRecord.modifiedLinkset] = linkSet;
                }

                if (this.crudService.modifiedRecord.from === 'CrudView') {
                    this.crudService.updateRecord(params);
                    this.back();
                } else {
                    this.crudService.model = params;
                    this.back();
                }

            });
    }

    getLinkset(gridOptions) {
        let focusedRows = gridOptions.api.getSelectedRows();
        let result = [];

        return this.gridService.getTitleColumns(this.crudService.getLinkedClass())
            .then((columnName) => {
                for (let item = 0; item < focusedRows.length; item++) {
                    result['_' + item] = focusedRows[item]['@rid'];
                    result.push(focusedRows[item][columnName]);
                }

                return result;
            });
    }

}