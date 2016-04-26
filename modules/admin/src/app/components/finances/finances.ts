import {Component} from 'angular2/core';
import {TranslateService, TranslatePipe} from 'ng2-translate/ng2-translate';
import {Router} from 'angular2/router';
import {BreadcrumbService} from '../breadcrumb/breadcrumb.service';

@Component({
	selector: 'finances',
	templateUrl: 'app/components/finances/finances.html',
	styles: [
		require('./finances.scss')
	],
	providers: [BreadcrumbService],
	directives: [BreadcrumbService],
	pipes: [TranslatePipe]
})
export class Finances {

	constructor(public translate:TranslateService, public router:Router,
				public breadcrumb: BreadcrumbService) {
	}

	ngOnInit() {

	}

}