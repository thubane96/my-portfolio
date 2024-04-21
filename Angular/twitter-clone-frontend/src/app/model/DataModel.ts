
export interface UserSignup {
    firstName: string;
    lastName: string;
    username: string;
    password: string;
}

export interface UserLogin {
    username: string;
    password: string;
}

export interface TweetData {
    tweetBody: string;
}

export interface Tweet{
    id: number;
    tweetBody: string;
    tweetImage: any;
    tweetedBy: string;
    likes: number;
    time: string;
    isUserLiked: boolean;
    numberOfComments: number;
    tweetHasImage: boolean;
    userMuted: boolean;
    userBlocked: boolean;
    tweetPinned: boolean;
    tweetBookmarked: boolean;
    userFollowed: boolean;  
    tweetLikedBy: TweetLikedBy[];
    comments: Comment[];
    createdAt: any;
    updated: any;
}

export interface User{
    id: number;
    firstName: string;
    lastName: string;
    username: string;
    password: string;
    profilePicture: any;
    bio: string;
    birthDate: string;
    location: string;
    timeJoined: string;
    numFollowers: number;
    userFollowed: boolean;
    hasProfilePicture: boolean;
    arrayOfFollowers: string[];
    roles: Role[];
    notInterestedTweetList: NotInterestedTweet[];
    tweetMuteUserList: TweetMuteUser[];
    blockedUsersList: BlocledUsers[];
    followList: Follow[];
    pinnedTweet: PinnedTweet;
    TweetList: TweetList[];
    bookmarksList: Bookmarks[];
    messageList: Message[];
    hiddenTweetsList: HiddenTweets[];
    createdAt: any;
    updated: any;
}

export interface TweetLikedBy{
    id:number;
    likedBy: string;
    tweetId: number;
    createdAt: any;
    updated: any;
}

export interface Comment{
    id: number;
    commentBody: string;
    commentImage: any;
    commentedBy: string;
    commentLikes: number;
    numberOfCommentReplies: number;
    time: string;
    isUserLiked: boolean;
    commentHasImage: boolean;
    userFollowed: boolean;
    userMuted: boolean;
    userBlocked: boolean;
    commentLikedByList: CommentLikedBy[];
    tweetId: number;
    commentReplyList: CommentReply[];
    createdAt: any;
    updated: any;
}

export interface CommentLikedBy{
    id:number;
    likedBy: string;
    commentId: number;
    createdAt: any;
    updated: any;
}

export interface CommentReply{
    id: number;
    commentReplyBody: string;
    commentReplyImage: any;
    commentRepliedBy: string;
    commentReplyLikes: number;
    time: string;
    isUserLiked: boolean;
    commentReplyHasImage: boolean;
    userFollowed: boolean;
    userMuted: boolean;
    userBlocked: boolean;
    commentReplyLikedByList: CommentReplyLikedByList[];
    commentId: number;
    createdAt: any;
    updatedAt: any;
}

export interface CommentReplyLikedByList{
    id: number;
    likedBy: string;
    commentReplyId: number;
    createdAt: any;
    updated: any;
}

export interface Role{
    id: number;
    name: string;
    createdAt: any;
    updatedAt: any;
}

export interface NotInterestedTweet {
    id: number;
    tweetId: number;
    username: string;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface TweetMuteUser{
    id: number;
    usernameToMute: string;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface Follow{
    id: number;
    username: string;
    userId: number;
    createdAt: any;
    updatedAt: any;

}

export interface BlocledUsers{
    id: number;
    username: string;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface TweetList {
    id: number;
    tweetId: number;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface PinnedTweet{
    id: number;
    tweetId: number;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface Bookmarks {
    id: number;
    tweetId: number;
    userId: number;
    createdAt: any;
    updatedAt: any;
}

export interface HiddenTweets{
    id: number;
    tweetId: number;
    createdAt: any;
    updatedAt: any;
}

export interface Notifications {
    id: number;
    tweetId: number;
    username: string;
    notificationFrom: string;
    commentId: number;
    commmentReplyId;
    content: string;
    forTweet: boolean;
    forUser: boolean;
    forComment: boolean;
    forCommentReply: boolean;
    opened: boolean;
    createdAt: any;
    updatedAt: any;
}

export interface Message{
    id: number;
    friendMessaging: string;
    messagesList: Messages[];
    userId: number;
    numberOfUnopenedMessages: number;
    lastMessageContent: string;
    time: string;
    createdAt: any;
    updatedAt: any;
}

export interface Messages {
    id: number;
    messageBody: string;
    messageImage: any;
    messagedTo: boolean;
    opened: boolean;
    deleted: boolean;
    messageId: number;
    time: string;
    createdAt: any;
    updatedAt: any;
}
