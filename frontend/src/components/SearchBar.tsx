"use client";

import { Listbox, ListboxButton, ListboxOption, ListboxOptions, Transition } from "@headlessui/react";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/react/24/outline";
import { Fragment, useState } from "react";

export default () => {
    const [searchType, setSearchType] = useState(articleSearchTypes[1]);

    return (
        <form>
            <div className="flex lg:w-96">
                <Listbox value={searchType} onChange={(selected) => (setSearchType(selected))}>
                    {({ open }) => (
                        <div className="relative">
                            <ListboxButton className="relative w-full h-full cursor-default rounded-s-lg bg-gray-50 py-2 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 sm:text-sm sm:leading-6">
                                <span className="flex items-center">
                                    <span className="ml-3 block truncate">{searchType.value}</span>
                                </span>
                                <span className="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">
                                    <ChevronUpDownIcon aria-hidden="true" className="h-5 w-5 text-gray-400" />
                                </span>
                            </ListboxButton>

                            <Transition
                                show={open}
                                as={Fragment}
                                leave="transition ease-in duration-100"
                                leaveFrom="opacity-100"
                                leaveTo="opacity-0"
                            >
                                <ListboxOptions className="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-gray-50 py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                    {articleSearchTypes.map((articleSearchType) => (
                                        <ListboxOption
                                            key={articleSearchType.id}
                                            value={articleSearchType}
                                            className={({ focus }) =>
                                                initClassNames(
                                                    focus ? 'bg-indigo-600 text-white' : 'text-gray-900',
                                                    "relative cursor-default select-none py-2 pl-3 pr-0"
                                                )
                                            }
                                        >
                                            {({ selected, focus }) => (
                                                <>
                                                    <div className="flex items-center">
                                                        <span
                                                            className={
                                                                initClassNames(
                                                                    selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate'
                                                                )
                                                            }
                                                        >{articleSearchType.value}</span>
                                                    </div>
                                                    {selected ? (
                                                        <span
                                                            className={
                                                                initClassNames(
                                                                    focus ? 'text-white' : 'text-indigo-600', "absolute inset-y-0 right-0 flex items-center pr-4"
                                                                )
                                                            }
                                                        >
                                                            <CheckIcon aria-hidden="true" className="h-5 w-5" />
                                                        </span>
                                                    ) : null}
                                                </>
                                            )}
                                        </ListboxOption>
                                    ))}
                                </ListboxOptions>
                            </Transition>
                        </div>
                    )}
                </Listbox>
                <div className="relative w-full">
                    <input type="search" id="search-dropdown" className="block p-2.5 w-full z-20 text-sm text-gray-900 bg-gray-50 rounded-e-lg border-s-gray-50 border-s-2 border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-s-gray-700  dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:border-blue-500" placeholder="Search Mockups, Logos, Design Templates..." required />
                    <button type="submit" className="absolute top-0 end-0 p-2.5 text-sm font-medium h-full text-white bg-blue-700 rounded-e-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                        <svg className="w-4 h-4" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z" />
                        </svg>
                        <span className="sr-only">Search</span>
                    </button>
                </div>
            </div>
        </form >
    );
}

function initClassNames(...classes: string[]) {
    return classes.filter(Boolean).join(' ');
}

const articleSearchTypes = [
    {
        id: 1,
        key: "TITLE",
        value: "제목",
    },
    {
        id: 2,
        key: "CONTENT",
        value: "내용",
    },
    {
        id: 3,
        key: "HASHTAG",
        value: "해시태그"
    },
    {
        id: 4,
        key: "CREATOR",
        value: "작성자",
    },
];