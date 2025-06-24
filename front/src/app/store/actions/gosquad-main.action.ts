import {createActionGroup, emptyProps} from '@ngrx/store';

/**
 * Main actions.
 */
export const GosquadMainActions = createActionGroup({
  source: 'MAIN',
  events: {
    'reload app': emptyProps(),
    'reload app data': emptyProps()
  }
});
