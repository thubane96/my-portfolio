import { Pipe, PipeTransform } from '@angular/core';
import { User } from '../model/DataModel';

@Pipe({
  name: 'userFilter',
  pure: false
})
export class SearchUserPipe implements PipeTransform {

  transform(value: any, searchText: string, propName: string): any{
    
    if (value.length === 0){
      return value;
    }
    const resultArray = [];
    for(const item of value){
      if (searchText.length > 0){
        if (item.firstName.toLocaleLowerCase().includes(searchText.toLocaleLowerCase()) || 
        item.lastName.toLocaleLowerCase().includes(searchText.toLocaleLowerCase())){
          resultArray.push(item);
        }
      }
    }
    return resultArray;
  }

}
