function [ num_matches,matches,dist_vals ] = match(image1, image2, distRatio)
%match Find matches between image1 and image2

    % Find SIFT keypoints for each image
    [im1, des1, loc1] = sift(image1);
    [im2, des2, loc2] = sift(image2);

    % For each descriptor in the first image, select its match to second image.
    des2t = des2';                          % Precompute matrix transpose
    matches_indexes = zeros(size(des1,1), 1);
    dist_vals = zeros(size(des1,1), 1);
    for i = 1 : size(des1,1)
       dotprods = des1(i,:) * des2t;        % Computes vector of dot products
       [vals,indx] = sort(acos(dotprods));  % Take inverse cosine and sort results

       % Check if nearest neighbor has angle less than distRatio times 2nd.
       if (vals(1) < distRatio * vals(2))
          matches_indexes(i) = indx(1);
          dist_vals(i) = vals(1) / vals(2);
       else
          matches_indexes(i) = 0;
          dist_vals(i) = 0;
       end
    end
    num_matches = sum(matches_indexes > 0);
    cols1 = size(im1,2);
    matches = zeros(num_matches, 4);
    k = 1;
    for i = 1:size(matches_indexes, 1)
        j = matches_indexes(i);
        if (j > 0)
            matches(k, :) = [loc1(i,1), loc1(i,2), loc2(j, 1), loc2(j,2)+cols1];
            k = k +1;
        end
    end

%     % Create a new image showing the two images side by side.
%     im3 = appendimages(im1,im2);
% 
%     % Show a figure with lines joining the accepted matches.
%     figure('Position', [100 100 size(im3,2) size(im3,1)]);
%     colormap('gray');
%     imagesc(im3);
%     hold on;
%     cols1 = size(im1,2);
%     for i = 1: size(des1,1)
%       if (matches(i) > 0)
%         line([loc1(i,2) loc2(matches(i),2)+cols1], ...
%              [loc1(i,1) loc2(matches(i),1)], 'Color', 'c');
%       end
%     end
%     hold off;
%     num_matches = sum(matches > 0);
%     fprintf('Found %d matches.\n', num_matches);    
end

