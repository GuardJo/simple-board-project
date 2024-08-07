export default function HashtagBadge({ hashtagName }: Props) {
    return (
        <span className={initBadgeStyle()}>{hashtagName}</span>
    );
}

function initBadgeStyle(): string {
    let styleNumber: number = Math.floor(Math.random() * 7);

    return badgeStyles[styleNumber];
}

const badgeStyles = [
    "inline-flex items-center rounded-lg bg-gray-50 px-2 py-1 text-md font-medium text-gray-600 ring-1 ring-inset ring-gray-500/10",
    "inline-flex items-center rounded-lg bg-red-50 px-2 py-1 text-md font-medium text-red-700 ring-1 ring-inset ring-red-600/10",
    "inline-flex items-center rounded-lg bg-yellow-50 px-2 py-1 text-md font-medium text-yellow-800 ring-1 ring-inset ring-yellow-600/20",
    "inline-flex items-center rounded-lg bg-green-50 px-2 py-1 text-md font-medium text-green-700 ring-1 ring-inset ring-green-600/20",
    "inline-flex items-center rounded-lg bg-blue-50 px-2 py-1 text-md font-medium text-blue-700 ring-1 ring-inset ring-blue-700/10",
    "inline-flex items-center rounded-lg bg-indigo-50 px-2 py-1 text-md font-medium text-indigo-700 ring-1 ring-inset ring-indigo-700/10",
    "inline-flex items-center rounded-lg bg-purple-50 px-2 py-1 text-md font-medium text-purple-700 ring-1 ring-inset ring-purple-700/10",
    "inline-flex items-center rounded-lg bg-pink-50 px-2 py-1 text-md font-medium text-pink-700 ring-1 ring-inset ring-pink-700/10",
];

interface Props {
    hashtagName: string,
};