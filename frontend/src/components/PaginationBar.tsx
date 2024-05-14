import ChevronLeftIcon from "@heroicons/react/24/outline/ChevronLeftIcon"
import ChevronRightIcon from "@heroicons/react/24/outline/ChevronRightIcon"

const defaultRange = 5;

const currentPageStyle = "relative z-10 inline-flex items-center bg-indigo-600 px-4 py-2 text-sm font-semibold text-white focus:z-20 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600";
const defaultStyle = "relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-700 ring-1 ring-inset ring-gray-300 focus:outline-offset-0";

function getPageIndex(number: number, totalPage: number): number[] {
    let startNumber = (number == totalPage) ? totalPage - defaultRange + 1 : Math.max((number - (Math.ceil(defaultRange / 2))), 1)
    let finishNumber = Math.min(startNumber + defaultRange, totalPage + 1);

    return [startNumber, finishNumber];
}

function initPages(number: number, totalPage: number, searchType?: string, searchValue?: string) {
    const result = [];
    let [startNumber, finishNumber] = getPageIndex(number, totalPage);

    for (let i = startNumber; i < finishNumber; i++) {
        if (i == number) {
            result.push(
                <a
                    href={makeAnchorLink(i, searchType, searchValue)}
                    className={currentPageStyle}
                >
                    {i}
                </a>
            );
        } else {
            result.push(
                <a
                    href={makeAnchorLink(i, searchType, searchValue)}
                    className={(i == 0) ? currentPageStyle : defaultStyle}
                >
                    {i}
                </a>
            );
        }
    }

    return result;
}

function makeAnchorLink(nextPage: number, searchType?: string, searchValue?: string) {
    searchType = searchType ?? "TITLE";
    searchValue = searchValue ?? "";

    return `/articles?page=${nextPage}&searchType=${searchType}&searchValue=${searchValue}`;
}

export default ({ number, totalPage, searchType, searchValue }: Props) => {
    const prevLink = Math.max(number - 1, 1);
    const nextLink = Math.min(number + 1, totalPage)

    return (
        <div className="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6 lg:w-[1000px]">
            <div className="flex flex-1 justify-between sm:hidden">
                <a
                    href={makeAnchorLink(prevLink, searchType, searchValue)}
                    className="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
                >
                    Previous
                </a>
                <a
                    href={makeAnchorLink(nextLink, searchType, searchValue)}
                    className="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
                >
                    Next
                </a>
            </div>
            <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
                <div>
                    <p className="text-sm text-gray-700">
                        Showing <span className="font-medium">10</span> to <span className="font-medium">{number}</span> of{' '}
                        <span className="font-medium">{totalPage}</span> results
                    </p>
                </div>
                <div>
                    <nav className="isolate inline-flex -space-x-px rounded-md shadow-sm" aria-label="Pagination">
                        <a
                            href={makeAnchorLink(prevLink, searchType, searchValue)}
                            className="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0"
                        >
                            <span className="sr-only">Previous</span>
                            <ChevronLeftIcon className="h-5 w-5" aria-hidden="true" />
                        </a>
                        {initPages(number, totalPage, searchType, searchValue)}
                        <a
                            href={makeAnchorLink(nextLink, searchType, searchValue)}
                            className="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-inset ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0"
                        >
                            <span className="sr-only">Next</span>
                            <ChevronRightIcon className="h-5 w-5" aria-hidden="true" />
                        </a>
                    </nav>
                </div>
            </div>
        </div>
    )
}

interface Props {
    number: number,
    totalPage: number,
    searchType?: string,
    searchValue?: string,
};