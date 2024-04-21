import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'tweetSearch'
})
export class TweetSearchPipe implements PipeTransform {

  transform(value: any, searchText: string, propName: string): any{
    
    if (value.length === 0){
      return value;
    }
    const resultArray = [];
    for(const item of value){
      if (searchText.length > 0){
        if (item.tweetBody.toLocaleLowerCase().includes(searchText.toLocaleLowerCase())){
          resultArray.push(item);
        }
      }
    }
    return resultArray;
  }

}
